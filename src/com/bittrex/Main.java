package com.bittrex;

import java.util.*;

public class Main {
//    static final String SITE = "https://bittrex.com/api/v1.1/";
//    static final String API_KEY = "28a82ab3aee54e139f3005518859adf0";
    static String storedCoinName = "";
    static String storedUuid = "";
    static Map<String, Order> mySellOrders = new HashMap<>();


    private static void printMenu() {
        System.out.println();
        System.out.println("PUBLIC\n======");
        System.out.println("1 - Get BTC Balance");
        System.out.println("2 - Get all balances");
        System.out.println("3 - Get open orders");
        System.out.println("4 - Get order history");
        System.out.println("5 - Get order by uuid");
        System.out.println("6 - Get market summary");

        System.out.println("MARKET\n======");
        System.out.println("11 - Buy");
        System.out.println("0 - Exit");
    }

    private static String getCoinName() {
        System.out.print("Input coin name");
        if (!"".equals(storedCoinName)) {
            System.out.print(" (Enter for " + storedCoinName + ")");
        }
        System.out.print(":");
        String coinName = new Scanner(System.in).nextLine().toUpperCase();
        if (!"".equals(coinName)){
            storedCoinName = coinName;
        }
        return (storedCoinName);
    }



    private static double getQuantity(String message) {
        System.out.print(message);
        Scanner scanner = new Scanner(System.in);
        boolean inputIsCorrect = true;
        double quantity = 0;
        do {
            try {
                quantity = scanner.nextDouble();
                if (quantity > 0) {
                    inputIsCorrect = true;
                } else throw new NoSuchElementException();
            } catch (RuntimeException e) {
                scanner.nextLine();
                System.out.println("Incorrect input. Try again");
            }
        } while (!inputIsCorrect);
        return quantity;
    }

    private static BittrexReply buy(Bittrex wrapper, String coin) {
        double volumeToBuy = getQuantity("Enter buy volume in BTC: ");
        BittrexReply marketSummary = getMarketSummary(wrapper, coin);
        double lastPrice = Double.valueOf(marketSummary.result.get(0).get("Last"));
        String lastPriceToString = String.format("%.9f", lastPrice * 1.2);
        double quantityToBuy = volumeToBuy / lastPrice;
        String quantityToBuyString = String.format("%.9f", quantityToBuy);
        String rawResponse = wrapper.buyLimit("BTC-" + coin, quantityToBuyString, lastPriceToString);
        BittrexReply bittrexReply = wrapper.getMapsFromResponse(rawResponse);
//        HashMap<String, String> onlyMap = responseMapList.get(0);
        return bittrexReply;
    }

    private static BittrexReply fillSellOrder(Bittrex wrapper, String buyOrderUuid){
        System.out.println("Starting fillSellOrder procedure...");
        String rawResponse;
        BittrexReply buyOrder;
        boolean buyOrderIsClosed;
        int counter = 10;
        do {
            System.out.println("counter = " + counter);
            System.out.println("Making getOrder request");
            rawResponse = wrapper.getOrder(buyOrderUuid);
            System.out.print("Starting getMapsFromResponse procedure........ ");
            buyOrder = wrapper.getMapsFromResponse(rawResponse);
            System.out.println("Done!");
            System.out.println(buyOrder);
            buyOrderIsClosed = "false".equals(buyOrder.result.get(0).get("IsOpen"));
            System.out.println("buyOrderIsClosed = " + buyOrderIsClosed);
            System.out.println("buyOrder.result.get(0).get(\"IsOpen\") = " + buyOrder.result.get(0).get("IsOpen"));
            if (!buyOrderIsClosed){
                System.out.println("Buy order is still not closed. Waiting for 1 sec.");
                counter--;
                System.out.println("Waiting for 1 sec...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Stop waiting! Try again");
            }
        } while (!buyOrderIsClosed && counter>0);

        System.out.println("We are out of cycle");

        if (!buyOrderIsClosed){
            System.out.println("Buy order is fault!");
            return new BittrexReply();
        }

        System.out.println("Continuing...");
        String sellOrderUuid = buyOrder.result.get(0).get("OrderUuid");
        double buyPrice = Double.parseDouble(buyOrder.result.get(0).get("PricePerUnit"));
        String limitPrice =  String.valueOf(buyPrice/1.1);
        limitPrice = limitPrice.substring(0,limitPrice.indexOf('.')+8);
        String conditionTarget = String.valueOf(buyPrice/1.05);
        conditionTarget = conditionTarget.substring(0,conditionTarget.indexOf('.')+8);
        String market = buyOrder.result.get(0).get("Exchange");
        String quantity = buyOrder.result.get(0).get("Quantity");
        rawResponse = wrapper.sellLimit(market, String.valueOf(quantity),limitPrice,"LESS_THAN", conditionTarget);
        Order order = new Order(market,buyPrice);
        mySellOrders.put(sellOrderUuid,order);
        return wrapper.getMapsFromResponse(rawResponse);
    }

    private static BittrexReply getBalance(Bittrex wrapper, String coin) {
        String rawResponse = wrapper.getBalance(coin);
//        List<HashMap<String, String>> responseMapList = Bittrex.getMapsFromResponse(rawResponse);
        BittrexReply bittrexReply = wrapper.getMapsFromResponse(rawResponse);
//        HashMap<String, String> onlyMap = responseMapList.get(0);

//        System.out.println("debug print responseMapList:");
//        System.out.println(responseMapList);
//        System.out.println("debug print onlyMap:");
//        System.out.println(onlyMap);

        return bittrexReply;
    }

    private static BittrexReply getBalances(Bittrex wrapper) {
        String rawResponse = wrapper.getBalances();
//        List<HashMap<String, String>> responseMapList = Bittrex.getMapsFromResponse(rawResponse);
        BittrexReply bittrexReply = wrapper.getMapsFromResponse(rawResponse);
//        HashMap<String, String> onlyMap = responseMapList.get(0);
        return bittrexReply;

    }

    private static BittrexReply getOpenOrders(Bittrex wrapper) {
        String rawResponse = wrapper.getOpenOrders();
//        List<HashMap<String, String>> responseMapList = Bittrex.getMapsFromResponse(rawResponse);
        BittrexReply bittrexReply = wrapper.getMapsFromResponse(rawResponse);
//        HashMap<String, String> onlyMap = responseMapList.get(0);
        return bittrexReply;
    }

    private static BittrexReply getOrder(Bittrex wrapper, String uuid) {
        String rawResponse = wrapper.getOrder(uuid);
//        List<HashMap<String, String>> responseMapList = Bittrex.getMapsFromResponse(rawResponse);
        BittrexReply bittrexReply = wrapper.getMapsFromResponse(rawResponse);
//        HashMap<String, String> onlyMap = responseMapList.get(0);
        return bittrexReply;
    }

    private static BittrexReply getOrderHistory(Bittrex wrapper){
        String rawResponse = wrapper.getOrderHistory();
        BittrexReply bittrexReply = wrapper.getMapsFromResponse(rawResponse);
        return bittrexReply;
    }

    private static BittrexReply getMarketSummary(Bittrex wrapper, String coin) throws OperationIsFault {
        String rawResponse = wrapper.getMarketSummary("BTC-" + coin);
//        List<HashMap<String, String>> responseMapList = Bittrex.getMapsFromResponse(rawResponse);
        BittrexReply bittrexReply = wrapper.getMapsFromResponse(rawResponse);
//        HashMap<String, String> onlyMap = responseMapList.get(0);
        return bittrexReply;
    }

    public static void main(String[] args) {

        Bittrex wrapper = new Bittrex();
        wrapper.setAuthKeysFromTextFile("keys.txt");


        System.out.println("Trading at BittrexController.com");
        boolean inputIsCorrect = false;
        Scanner scanner = new Scanner(System.in);
        int choiceNumber = 0;
        printMenu();

        do {
            System.out.println("99 - Print menu");
            do {
                try {
                    choiceNumber = scanner.nextInt();
                    if (choiceNumber >= 0 && choiceNumber <= 99) {
                        inputIsCorrect = true;
                    } else throw new NoSuchElementException();
                } catch (NoSuchElementException e) {
                    scanner.nextLine();
                    System.out.println("Incorrect input. Try again");
                }
            } while (!inputIsCorrect);

            switch (choiceNumber) {
                case 1:
                    BittrexReply balance = getBalance(wrapper, "BTC");
                    System.out.println(balance);
                    break;

                case 2:
                    System.out.println("Balances:\n");
                    BittrexReply balances = getBalances(wrapper);
                    System.out.println("Balances: " + balances);
                    break;

                case 3:
                    BittrexReply orders = getOpenOrders(wrapper);
                    System.out.println(orders);
                    break;

                case 4:
                    BittrexReply orderHistory = getOrderHistory(wrapper);
                    System.out.println(orderHistory);
                    break;

                case 5:
                    System.out.print("Enter order uuid: ");
                    String uuid = scanner.nextLine();
                    uuid = scanner.nextLine();
                    BittrexReply order = getOrder(wrapper, uuid);
                    System.out.println(order);
                    break;

                case 6:
                    BittrexReply reply = getMarketSummary(wrapper, getCoinName());
                    System.out.println(reply);
                    break;

                case 11:
                    BittrexReply buy = buy(wrapper, getCoinName());
                    System.out.println(buy);

//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    BittrexReply sellStopOrders = fillSellOrder(wrapper, buy.result.get(0).get("uuid"));
//                    System.out.println(sellStopOrders);
                    break;

                case 99:
                    printMenu();
                    break;
            }
        } while (choiceNumber != 0);
        System.out.println("Exiting...");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

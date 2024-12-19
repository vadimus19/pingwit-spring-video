package com.pingwit.part_41.homework.task_1;

import java.util.List;
import java.util.Scanner;

public class StockApplication {
    public static void main(String[] args) {
        StockDatabaseConnectorService connectorService = new StockDatabaseConnectorService();
        StockRepository repository = new StockRepository(connectorService);

        repository.createStocksTable();
        repository.insertSampleData();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert a page number (1-4): ");
        int pageNumber = scanner.nextInt();

        int pageSize = 5;

        List<String> stocks = repository.getStocksByPage(pageNumber, pageSize);
        System.out.println("On page" + pageNumber + ":");
        stocks.forEach(System.out::println);
    }
}


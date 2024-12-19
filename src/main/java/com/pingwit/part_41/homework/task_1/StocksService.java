package com.pingwit.part_41.homework.task_1;

import java.util.List;

public class StocksService {
    public static void main(String[] args) {
        StockDatabaseConnectorService connectorService = new StockDatabaseConnectorService();
        StockRepository repository = new StockRepository(connectorService);

        repository.createStocksTable();
        repository.insertSampleData();

        List<String> topStocks = repository.getTop3ExpensiveStocksWithinWeek();
        System.out.println("Top3ExpensiveStocksWithinWeek:");
        topStocks.forEach(System.out::println);
    }
}
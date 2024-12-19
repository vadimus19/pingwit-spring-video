package com.pingwit.part_41.homework.task_1;


public class StockService {
    public static void main(String[] args) {
        StockDatabaseConnectorService connectorService = new StockDatabaseConnectorService();
        StockRepository repository = new StockRepository(connectorService);
        repository.createStocksTable();

    }
}

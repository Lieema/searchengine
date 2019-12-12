package service.query;

import service.compute.ComputeIDF;

public class BaseQuery extends Query {
    ComputeIDF computer = new ComputeIDF();

    @Override
    public ComputeIDF getComputeIDF() {
        return computer;
    }
}

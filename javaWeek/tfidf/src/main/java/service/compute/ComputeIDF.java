package service.compute;

import java.util.List;

public class ComputeIDF {
    public static Double computeIDF(final Double corpusSize, final Double matchingNumber) {
        Double res = Math.log(corpusSize / ((double)1 + matchingNumber));

        return res;
    }

    public List<Double> normalize(List<Double> w2v){
        Double norm = 0.0;
        for(Double x: w2v){
            norm += x*x;
        }
        norm = Math.sqrt(norm);
        for(Double x: w2v){
            x = x/norm;
        }
        return w2v;
    }

    public double dotProd(List<Double> a, List<Double> b){
        if(a.size() != b.size()){
            throw new IllegalArgumentException("The dimensions have to be equal!");
        }
        double sum = 0;
        for(int i = 0; i < a.size(); i++){
            sum += a.get(i) * b.get(i);
        }
        return sum;
    }

    public double distance(List<Double> a, List<Double> b) {
        if(a.size() != b.size()){
            throw new IllegalArgumentException("The dimensions have to be equal!");
        }
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.size(); i++) {
            diff_square_sum += (a.get(i) - b.get(i)) * (a.get(i) - b.get(i));
        }
        return Math.sqrt(diff_square_sum);
    }

}

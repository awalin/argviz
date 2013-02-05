package edu.umd.cs.argviz.client;

public class RankingItem<A> implements Comparable<RankingItem<A>> {
    private A object;
    private double value;
    private int ranking;
    
    /** Creates a new instance of Ranking */
    public RankingItem(A obj, double value) {
        this.object = obj;
        this.value = value;
    }

    public A getObject() {
        return object;
    }

    public int getRankingOrder(){
        return this.ranking;
    }
    
    public void setRankingOrder(int ranking){
        this.ranking = ranking;
    }
    
    public double getRankValue() {
        return value;
    }

    public void setRankValue(double v) {
        this.value = v;
    }
    // implement compareTo method of the Comparable interface to facilitate sorting
    @Override
    public int compareTo(RankingItem r) {
        return -(Double.compare(this.getRankValue(), r.getRankValue()));
    }
    
    @Override
    public int hashCode(){
        return this.object.hashCode();
    }

    // override equals method to facilitate entry searching
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (this.getClass() != obj.getClass())) {
            return false;
        }
        RankingItem r = (RankingItem) (obj);

        return (this.object.equals(r.getObject()));
    }

    @Override
    public String toString() {
        return (this.object + "\t" + Double.toString(value));
    }
}
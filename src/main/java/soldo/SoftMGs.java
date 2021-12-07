/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soldo;

import static soldo.Constants.MAX_BALANCE_Stop_mg_centesimo;
import static soldo.Constants.MAX_softMG_PAYOUT_centesimo;

/**
 *
 * @author zoi
 */
public class SoftMGs {



    private static final double ORDINARY_DIVIDER = 86400d;
    private long balance = 0l;
    private long amount = 0l;
    private long payout = 0l;
    private int beforeStamp = 0;
    private int afterStamp = 0;
    private double multiplier = 0;
    private long genesisEmission = 0l;
    private int lastForgedBlockHeight = 0;
    private long hold = 0l;
    private long AccouuntID = 0l;
    

    public boolean calculatePyoutSet() {
        double multi = 1d;
        double percent = 0d;
        double stpKof = 1d;
        
        if (balance>=1l && balance<=999999l) percent = 1d;
        if (balance>=1000000l && balance<=9999999999l) percent = 0.25d;
        if (balance>=10000000000l && balance<=99999999999l) percent = 0.5d;
        
        int yaForgu = Soldo.getBlockchain().OnforginBlock(AccouuntID, Constants.GlubinaPoiska);
        if(percent>0 && yaForgu>0 ){
            percent=1d;
        }
        if (amount>=100000000000l         && amount<=999999999999l) multi = 1.2d; 
        if (amount>=1000000000000l        && amount<=9999999999999l) multi = 1.5d;
        if (amount>=10000000000000l       && amount<=99999999999999l) multi = 1.8d; 
        if (amount>=100000000000000l) multi = 2d;                                    


        if (genesisEmission>=30000000000000000l    && genesisEmission<=44999999999999999l) stpKof = 0.85d;  
        if (genesisEmission>=45000000000000000l    && genesisEmission<=59999999999999999l) stpKof = 0.7d; 
        if (genesisEmission>=60000000000000000l    && genesisEmission<=74999999999999999l) stpKof = 0.55d;
        if (genesisEmission>=75000000000000000l    && genesisEmission<=89999999999999999l) stpKof = 0.4d; 
        if (genesisEmission>=90000000000000000l    && genesisEmission<=99999999999999999l) stpKof = 0.25d; 
        if (genesisEmission>=100000000000000000l) stpKof = 0.15d;      
        
        this.multiplier = (multi * percent * stpKof) / 100d;
        double days = (afterStamp - beforeStamp) / ORDINARY_DIVIDER;
  
        
        payout = (long) Math.floor((double) balance * (days * this.multiplier));
        
        if (payout < 0) payout = 0;
        if (payout > MAX_softMG_PAYOUT_centesimo) payout = MAX_softMG_PAYOUT_centesimo;
        if ((balance + payout) > MAX_BALANCE_Stop_mg_centesimo) payout =MAX_BALANCE_Stop_mg_centesimo - balance;

        return true;
    }


    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
    
    public long getAccountID() {
        return AccouuntID;
    }

    public void setAccountID(long accountID) {
        this.AccouuntID = accountID;
    }

    public long getPayout() {
        return payout;
    }

    public int getBeforeStamp() {
        return beforeStamp;
    }

    public void setBeforeStamp(int beforeStamp) {
        this.beforeStamp = beforeStamp;
    }

    public int getAfterStamp() {
        return afterStamp;
    }

    public void setAfterStamp(int afterStamp) {
        this.afterStamp = afterStamp;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setPayout(long payout) {
         this.payout = payout;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public int getLastForgedBlockHeight() {
        return lastForgedBlockHeight;
    }

    public void setLastForgedBlockHeight(int lastForgedBlockHeight) {
        this.lastForgedBlockHeight = lastForgedBlockHeight;
    }

    public long getHold() {
        return hold;
    }

    public void setHold(long hold) {
        this.hold = hold;
    }

    public boolean isOnHoldAtHeight(int height) {
        return getLastForgedBlockHeight() >= height - Constants.HOLD_RANGE 
                && balance >= Constants.HOLD_BALANCE_MIN 
                && balance <= Constants.HOLD_BALANCE_MAX; 
    }
    
    public void setGenesisEmission(long genesisEmission) {
        if (genesisEmission < 0) genesisEmission=genesisEmission*(-1);
        this.genesisEmission = genesisEmission;
    }

    @Override
    public String toString() {
        return "SMGMetrics{" + "balance=" + balance + ", amount=" + amount + ", payout=" + payout + ", beforeStamp=" + beforeStamp + ", afterStamp=" + afterStamp + ", multiplier=" + multiplier +'}';
    }

    


}

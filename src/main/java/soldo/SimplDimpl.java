/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soldo;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import soldo.util.Convert;
import soldo.util.Logger;

/**
 *
 * @author du44
 */
public class SimplDimpl {
    
    public static JSONObject getSLDgenerators(){
        Block lastBlock = Soldo.getBlockchain().getLastBlock();
        int _height = lastBlock.getHeight();
        Set<Long> list_gen = BlockDb.getBlockGenerators(Math.max(1, _height - 10000));
        JSONObject json_list = new JSONObject();
        try {
        HashMap<Long,JSONObject> hm = new HashMap<>(); 
        long inx =1;
        for (Iterator<Long> iterator = list_gen.iterator(); iterator.hasNext();) {
           JSONObject json_ = new JSONObject();
            Long ac_ID = iterator.next();
            String rs = Convert.rsAccount(ac_ID);
            long _effectBL = Account.getAccount(ac_ID).getEffectiveBalanceSLD(_height);
            json_.put("rs", rs); 
            json_.put("id", ac_ID);
            json_.put("effectBalans", _effectBL);
            hm.put(_effectBL,json_);
        }
         Map<Long, JSONObject> treeMap = new TreeMap<>(hm);
         int a=0;   
         for (Map.Entry<Long, JSONObject> entry : treeMap.entrySet()) {
                json_list.put(++a, entry.getValue() );
            }
         } catch (Exception e) {
             Logger.logErrorMessage("Failed to get SLD generators  " , e);
        }
        return json_list;
    }
    
     public static boolean verifyHit(BigInteger hit, BigInteger effectiveBalance, Block previousBlock, int timestamp) {
        
        int elapsedTime = timestamp - previousBlock.getTimestamp();
        if (elapsedTime <= 0) {
            return false;
        }
        BigInteger effectiveBaseTarget = BigInteger.valueOf(previousBlock.getBaseTarget()).multiply(effectiveBalance);
        BigInteger prevTarget = effectiveBaseTarget.multiply(BigInteger.valueOf(elapsedTime - 1));
        BigInteger target = prevTarget.add(effectiveBaseTarget);
        return hit.compareTo(target) < 0
                && (hit.compareTo(prevTarget) >= 0
                || (Constants.isTestnet ? elapsedTime > 300 : elapsedTime > 3600)
                || Constants.isOffline);
    }
    
}

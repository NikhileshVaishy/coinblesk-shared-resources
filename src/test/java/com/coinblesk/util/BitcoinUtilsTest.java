/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import java.util.Arrays;
import java.util.List;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.params.UnitTestParams;
import org.bitcoinj.testing.FakeTxBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author draft
 */
public class BitcoinUtilsTest {
    
    @Test
    public void sortTestOutputs() {
        Transaction tx = FakeTxBuilder.createFakeTx(UnitTestParams.get(), Coin.COIN, new ECKey());
        TransactionOutput to1 = tx.addOutput(Coin.valueOf(1), new ECKey());
        TransactionOutput to2 = tx.addOutput(Coin.valueOf(2), new ECKey());
        TransactionOutput to3 = tx.addOutput(Coin.valueOf(3), new ECKey());
        
        TransactionInput ti1 = tx.addInput(to1);
        TransactionInput ti2 = tx.addInput(to2);
        TransactionInput ti3 = tx.addInput(to3);
        
        List<TransactionOutput> txs1 = Arrays.asList(to1, to2, to3);
        List<TransactionOutput> txs2 = Arrays.asList(to2, to1, to3);
        List<TransactionOutput> txs3 = Arrays.asList(to3, to2, to1);
        List<TransactionOutput> txs4 = Arrays.asList(to3, to1, to2);
        List<TransactionOutput> txs5 = Arrays.asList(to1, to3, to2);
        List<TransactionOutput> txs6 = Arrays.asList(to2, to3, to1);
        
        List<TransactionOutput> reference = BitcoinUtils.sortOutputs(txs1);
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs1));
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs2));
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs3));
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs4));
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs5));
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs6));
        Assert.assertNotEquals(txs1, txs2);
        
    }
    
    @Test
    public void sortTestInputs() {
        Transaction tx = FakeTxBuilder.createFakeTx(UnitTestParams.get(), Coin.COIN, new ECKey());
        TransactionOutput to1 = tx.addOutput(Coin.valueOf(1), new ECKey());
        TransactionOutput to2 = tx.addOutput(Coin.valueOf(2), new ECKey());
        TransactionOutput to3 = tx.addOutput(Coin.valueOf(3), new ECKey());
        
        TransactionInput ti1 = tx.addInput(to1);
        TransactionInput ti2 = tx.addInput(to2);
        TransactionInput ti3 = tx.addInput(to3);
        
        List<TransactionInput> txs1 = Arrays.asList(ti1, ti2, ti3);
        List<TransactionInput> txs2 = Arrays.asList(ti2, ti1, ti3);
        List<TransactionInput> txs3 = Arrays.asList(ti3, ti2, ti1);
        List<TransactionInput> txs4 = Arrays.asList(ti3, ti1, ti2);
        List<TransactionInput> txs5 = Arrays.asList(ti1, ti3, ti2);
        List<TransactionInput> txs6 = Arrays.asList(ti2, ti3, ti1);
        
        List<TransactionInput> reference = BitcoinUtils.sortInputs(txs1);
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs1));
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs2));
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs3));
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs4));
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs5));
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs6));
        Assert.assertNotEquals(txs1, txs2);
        
    }
}
package com.coinblesk.bitcoin;

import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKLOCKTIMEVERIFY;
import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIG;
import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIGVERIFY;
import static org.bitcoinj.script.ScriptOpCodes.OP_DROP;
import static org.bitcoinj.script.ScriptOpCodes.OP_ELSE;
import static org.bitcoinj.script.ScriptOpCodes.OP_ENDIF;
import static org.bitcoinj.script.ScriptOpCodes.OP_IF;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.bitcoinj.core.Transaction.SigHash;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptOpCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Andreas Albrecht
 *
 */
public class TimeLockedAddressTest {
	private static final Logger LOG = LoggerFactory.getLogger(TimeLockedAddressTest.class);
	
	private ECKey userKey, serviceKey;
	private byte[] userPubKey, servicePubKey;
	
	// do not change, addresses below are precalculated for this locktime
	private final long lockTime = 123456;
	
	// expected addresses for given keys and locktime
	private final ECKey FIXED_USER_KEY = 
			ECKey.fromPrivate(Sha256Hash.hash("user-alice".getBytes()));
	private final ECKey FIXED_SERVER_KEY = 
			ECKey.fromPrivate(Sha256Hash.hash("service-bob".getBytes()));
	private final String MAINNET_ADDRESS = "34DSzRDMVxZZsonGf3zA98vLgt5o1T4EBx";
	private final String TESTNET_ADDRESS = "2Mumf4A9P7R4v5bQpLBc2m5ubuEHxjb4jy4";
	
	private NetworkParameters defaultParams;
	private TestNet3Params testnet;
	private MainNetParams mainnet;
	
	@Before
	public void before() {
		testnet = TestNet3Params.get();
		mainnet = MainNetParams.get();
		defaultParams = mainnet;
		createKeys();
	}
	
	@After
	public void after() {
		userKey = null;
		userPubKey = null;
		serviceKey = null;
		servicePubKey = null;
		defaultParams = null;
	}
	
	private void createKeys() {
		userKey = new ECKey();
		userPubKey = userKey.getPubKey();
		serviceKey = new ECKey();
		servicePubKey = serviceKey.getPubKey();
	}
	
	@Test
	public void testPrint() {
		TimeLockedAddress tla = createTimeLockedAddress();
		LOG.info(tla.toString());
		LOG.info(tla.toString(defaultParams));
		LOG.info(tla.toStringDetailed(defaultParams));
		
		LOG.info(tla.toString(null));
		LOG.info(tla.toStringDetailed(null));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNoLockTime() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, 0);
		assertNull(tla);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNegativeLockTime() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, -1);
		assertNull(tla);
	}
	
	@Test
	public void testAddress() {
		TimeLockedAddress tla = createTimeLockedAddress();
		Address address = tla.getAddress(defaultParams);
		assertNotNull(address);
	}

	@Test
	public void testAddressHash() {
		TimeLockedAddress tlaMainNet = createTimeLockedAddress();
		assertNotNull(tlaMainNet.getAddressHash());
		assertTrue(tlaMainNet.getAddressHash().length == 20);
		
		TimeLockedAddress tlaTestNet = createTimeLockedAddress();
		assertNotNull(tlaTestNet.getAddressHash());
		assertTrue(tlaTestNet.getAddressHash().length == 20);
		
		// hash does not depend on network.
		assertTrue(Arrays.equals(tlaMainNet.getAddressHash(), tlaTestNet.getAddressHash()));
		assertNotEquals(tlaMainNet.getAddress(mainnet), tlaTestNet.getAddress(testnet));
	}
	
	@Test
	public void testAddressMainnet() {
		TimeLockedAddress tla = new TimeLockedAddress(FIXED_USER_KEY.getPubKey(), FIXED_SERVER_KEY.getPubKey(), lockTime);
		Address p2shAddress = tla.getAddress(mainnet);
		Address expectedAddr = Address.fromBase58(mainnet, MAINNET_ADDRESS);
		assertEquals(p2shAddress, expectedAddr);
		assertTrue(Arrays.equals(tla.getAddressHash(), expectedAddr.getHash160()));
	}
	
	@Test
	public void testAddressTestnet() {
		TimeLockedAddress tla = new TimeLockedAddress(FIXED_USER_KEY.getPubKey(), FIXED_SERVER_KEY.getPubKey(), lockTime);
		Address p2shAddress = tla.getAddress(testnet);
		Address expectedAddr = Address.fromBase58(testnet, TESTNET_ADDRESS);
		assertEquals(p2shAddress, expectedAddr);
		assertTrue(Arrays.equals(tla.getAddressHash(), expectedAddr.getHash160()));
	}
	
	@Test
	public void testEquals() {
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = createTimeLockedAddress();
		assertEquals(tThis, tOther);
		assertEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
		
		assertEquals(tThis, tThis);
		assertEquals(tOther, tOther);
		
		assertTrue(tThis.hashCode() == tOther.hashCode());
	}
	
	@Test
	public void testNotEquals_lockTime() {
		long lockTimeOther = lockTime+1;
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, servicePubKey, lockTimeOther);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
	}
	
	@Test
	public void testNotEquals_network() {
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = createTimeLockedAddress();
		assertEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(mainnet), tOther.getAddress(testnet));
		// address hash should still be the same!
		assertTrue(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_userkey() {
		ECKey userKeyOther = new ECKey();
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = new TimeLockedAddress(userKeyOther.getPubKey(), servicePubKey, lockTime);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_servicekey() {
		ECKey serviceKeyOther = new ECKey();
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, serviceKeyOther.getPubKey(), lockTime);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_keyswitch() {
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = new TimeLockedAddress(servicePubKey, userPubKey, lockTime);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals() {
		TimeLockedAddress tThis = createTimeLockedAddress();
		assertNotEquals(tThis, null);
		
		assertNotEquals(tThis, new Object());
	}
	
	@Test
	public void testFromRedeemScript() {
		TimeLockedAddress tla = createTimeLockedAddress();
		Script script = tla.createRedeemScript();
		
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(script.getProgram());
		assertEquals(tla, copyTla);
		assertEquals(tla.getAddress(defaultParams), copyTla.getAddress(defaultParams));
		assertEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	@Test
	public void testFromRedeemScriptHex() {
		TimeLockedAddress tla = createTimeLockedAddress();
		Script script = tla.createRedeemScript();
		String scriptHex = Utils.HEX.encode(script.getProgram());
			
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(scriptHex);
		assertEquals(tla, copyTla);
		assertEquals(tla.getAddress(defaultParams), copyTla.getAddress(defaultParams));
		assertEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
		
	@Test(expected=IllegalArgumentException.class)
	public void testFromRedeemScript_badScript() {
		TimeLockedAddress tla = createTimeLockedAddress();
		
		Script wrongScript = ScriptBuilder.createOutputScript(new ECKey().toAddress(defaultParams));
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(wrongScript.getProgram());
		assertNotEquals(tla, copyTla);
		assertNotEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromRedeemScript_badScript2() {
		TimeLockedAddress tla = createTimeLockedAddress();
		
		Script wrongScript = new ScriptBuilder()
				.op(OP_IF)
				.number(lockTime).op(OP_CHECKLOCKTIMEVERIFY).op(OP_DROP)
				.op(OP_ELSE)
				.data(servicePubKey).op(OP_CHECKSIGVERIFY)
				.op(OP_ENDIF)
				.data(userPubKey).op(OP_CHECKSIG)
				.build();
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(wrongScript.getProgram());
		assertNotEquals(tla, copyTla);
		assertNotEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	@Test
	public void testPubkeyScript() {
		TimeLockedAddress tla = createTimeLockedAddress();
		Address address = tla.getAddress(defaultParams);
		
		Script script = tla.createPubkeyScript();
		assertTrue(script.isPayToScriptHash());
		Address toAddress = script.getToAddress(defaultParams);
		assertEquals(address, toAddress);
	}
	
	@Test
	public void testScriptSigAfterLocktime() {
		TimeLockedAddress tla = createTimeLockedAddress();
		Script scriptSig = tla.createScriptSigAfterLockTime(TransactionSignature.dummy());
		
		assertEquals(scriptSig.getChunks().size(), 3);
		assertArrayEquals(scriptSig.getChunks().get(0).data, TransactionSignature.dummy().encodeToBitcoin());
		assertEquals(scriptSig.getChunks().get(1).opcode, ScriptOpCodes.OP_0);
		assertArrayEquals(scriptSig.getChunks().get(2).data, tla.createRedeemScript().getProgram());
	}
	
	@Test
	public void testScriptSigBeforeLocktime() {
		TimeLockedAddress tla = createTimeLockedAddress();
		TransactionSignature clientSig = new TransactionSignature(new ECKey().sign(Sha256Hash.of("client".getBytes())), SigHash.ALL, false);
		TransactionSignature serverSig = new TransactionSignature(new ECKey().sign(Sha256Hash.of("server".getBytes())), SigHash.ALL, false);
		Script scriptSig = tla.createScriptSigBeforeLockTime(clientSig, serverSig);
	
		assertEquals(scriptSig.getChunks().size(), 4);
		assertArrayEquals(scriptSig.getChunks().get(0).data, clientSig.encodeToBitcoin());
		assertArrayEquals(scriptSig.getChunks().get(1).data, serverSig.encodeToBitcoin());
		assertEquals(scriptSig.getChunks().get(2).opcode, ScriptOpCodes.OP_1);
		assertArrayEquals(scriptSig.getChunks().get(3).data, tla.createRedeemScript().getProgram());
	}
	
	@Test
	public void testLockTimeComparator() {
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = new TimeLockedAddress(tThis.getClientPubKey(), tThis.getServerPubKey(), tThis.getLockTime()+1);
		List<TimeLockedAddress> list = new ArrayList<>();
		list.add(tOther);
		list.add(tThis);
		
		Collections.sort(list, new TimeLockedAddress.LockTimeComparator(true));
		assertEquals(tThis, list.get(0));
		assertEquals(tOther, list.get(1));
		
		Collections.sort(list, new TimeLockedAddress.LockTimeComparator(false));
		assertEquals(tThis, list.get(1));
		assertEquals(tOther, list.get(0));
		
	}

	/** create address with default parameters */
	private TimeLockedAddress createTimeLockedAddress() {
		return new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
	}
	
}

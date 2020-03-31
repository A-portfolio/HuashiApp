package com.muxistudio.appcommon.user;


import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;

import com.muxistudio.appcommon.utils.SPUtils;
import com.muxistudio.common.base.Global;


import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

public class EncryptionUser {

    private static final String KEYSTORE_PROVIDER ="AndroidKeyStore";
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    private static final String KEYSTORE_ALIAS = "user_alias";

    private static final String SP_ENCRYPTION="encryption";
    private static final String SP_IV="ase_iv";

    private KeyHelperBelowApi23 keyHelper;
    private SPUtils spUtils;
    private KeyStore mKeyStore;


    public EncryptionUser(){
        spUtils= SPUtils.getInstance(SP_ENCRYPTION);


    }



    public synchronized String encryptAES(String content) throws Exception {
        Cipher encrypt=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            encrypt= Cipher.getInstance(AES_MODE);
            encrypt.init(Cipher.ENCRYPT_MODE,getSecretAESKeyApi23());
        }else {
            if (keyHelper==null) {
                keyHelper = new KeyHelperBelowApi23();
            }
            encrypt= Cipher.getInstance(AES_MODE);
            encrypt.init(Cipher.ENCRYPT_MODE,keyHelper.getAESKey());

        }
        byte[]iv=encrypt.getIV();
        byte[]encryptedBytes=encrypt.doFinal(content.getBytes());
        ByteBuffer byteBuffer=ByteBuffer.allocate(1+iv.length+encryptedBytes.length);
        byteBuffer.put((byte)iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedBytes);
        return Base64.encodeToString(byteBuffer.array(), Base64.DEFAULT);

    }


    public synchronized String decryptAES(String content)throws Exception {
        byte[]decodeBytes= Base64.decode(content, Base64.DEFAULT);
        ByteBuffer byteBuffer = ByteBuffer.wrap(decodeBytes);
        int ivLength=byteBuffer.get();
        byte []iv=new byte[ivLength];
        byteBuffer.get(iv);
        byte[]cipherText=new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);
        Cipher decrypt;
        decrypt= Cipher.getInstance(AES_MODE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decrypt.init(Cipher.DECRYPT_MODE,getSecretAESKeyApi23(),new GCMParameterSpec(128,iv));
        }else {
            if (keyHelper==null){
                keyHelper=new KeyHelperBelowApi23();
            }
            decrypt.init(Cipher.DECRYPT_MODE,keyHelper.getAESKey(),new GCMParameterSpec(128,iv));
        }



        return new String(decrypt.doFinal(cipherText));
    }


    private byte[] getIv(){
        String ivd=spUtils.getString(SP_IV);
        byte []iv=null;
        if (ivd.length()==0){
            SecureRandom random=new SecureRandom();
            byte[] generate=random.generateSeed(16);
            ivd= Base64.encodeToString(generate, Base64.DEFAULT);
            spUtils.put(SP_IV,ivd);
            Log.i("TAG", "getIv: "+new String(generate));
            return generate;
        }
        iv= Base64.decode(ivd, Base64.DEFAULT);
        return iv;
    }
    /**
     * AES_MODE = "AES/CBC/PKCS7Padding"
     * api大于23时直接获取 aes key
     *
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private SecretKey getSecretAESKeyApi23() throws Exception {
        mKeyStore= KeyStore.getInstance(KEYSTORE_PROVIDER);
        mKeyStore.load(null);
        if (mKeyStore.containsAlias(KEYSTORE_ALIAS)){
            return ((KeyStore.SecretKeyEntry)mKeyStore.getEntry(KEYSTORE_ALIAS,null)).getSecretKey();
        }
        final KeyGenerator keyGenerator= KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES,KEYSTORE_PROVIDER);

        keyGenerator.init(new KeyGenParameterSpec.Builder(KEYSTORE_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT| KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build());

        return keyGenerator.generateKey();

    }





    /**
     * api23以下用rsa+aes配合
     * api23以下无法直接使用对称加密
     * 所以用非对称+对称式加解密流程
     *  1.使用keystore产生一对rsa key
     *  2.用rsa公匙对aes的key加密存入本地
     *  3.使用时用keystore获取私匙解密得到aes的key
     *
     */
    private  class KeyHelperBelowApi23{


        private KeyStore keyStore;
        private static final String SP_AES="sp_aes";
        public KeyHelperBelowApi23() throws Exception {
            keyStore= KeyStore.getInstance(KEYSTORE_PROVIDER);
            keyStore.load(null);
            if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
                generateRSAKey();
            }
        }

        private void generateRSAKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 30);

            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(Global.getApplication())
                    .setAlias(KEYSTORE_ALIAS)
                    .setSubject(new X500Principal("CN=" + KEYSTORE_ALIAS))
                    .setSerialNumber(BigInteger.TEN)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                           .getInstance("RSA", KEYSTORE_PROVIDER);

            keyPairGenerator.initialize(spec);
            keyPairGenerator.generateKeyPair();

        }

        private SecretKeySpec getAESKey() throws Exception {
            String encryptAESKey=spUtils.getString(SP_AES);
            byte[]aesKey=null;
            if (encryptAESKey.length()==0){
                aesKey=new byte[16];
                SecureRandom random=new SecureRandom();
                random.nextBytes(aesKey);
                encryptAESKey=encryptRSA(aesKey);
                spUtils.put(SP_AES,encryptAESKey);
                return new SecretKeySpec(aesKey,AES_MODE);
            }
            aesKey=decryptRSA(encryptAESKey);
            return new SecretKeySpec(aesKey,AES_MODE);

        }

        private String encryptRSA(byte[] plainText) throws Exception {
            PublicKey publicKey = keyStore.getCertificate(KEYSTORE_ALIAS).getPublicKey();

            Cipher cipher = Cipher.getInstance(RSA_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedByte = cipher.doFinal(plainText);
            return Base64.encodeToString(encryptedByte, Base64.DEFAULT);
        }

        private byte[] decryptRSA(String encryptedText) throws Exception {
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEYSTORE_ALIAS, null);

            Cipher cipher = Cipher.getInstance(RSA_MODE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);

            return cipher.doFinal(encryptedBytes);
        }




    }








}

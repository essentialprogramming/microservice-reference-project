package com.authentication.security;

import com.authentication.exceptions.codes.ErrorCode;
import com.util.cloud.Environment;
import com.util.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class KeyStoreService {

    private static final Logger LOG = LoggerFactory.getLogger(KeyStoreService.class);

    private static final String PUBLIC_KEY_FILE_PATH = "PUBLIC_KEY_FILE_PATH";
    private static final String PRIVATE_KEY_FILE_PATH = "PRIVATE_KEY_FILE_PATH";
    private static final String DEFAULT_PUBLIC_KEY_FILE_PATH = "classpath:pem/public-key.pem";
    private static final String DEFAULT_PRIVATE_KEY_FILE_PATH = "classpath:pem/private-key.pem";

    private final String publicKeyFilePath;
    private final String privateKeyFilePath;

    public KeyStoreService() {

        publicKeyFilePath = Environment.getProperty(PUBLIC_KEY_FILE_PATH, DEFAULT_PUBLIC_KEY_FILE_PATH);
        privateKeyFilePath = Environment.getProperty(PRIVATE_KEY_FILE_PATH, DEFAULT_PRIVATE_KEY_FILE_PATH);
    }

    public PublicKey getPublicKey() {
        try {
            return PemUtils.readPublicKeyFromPEMFile(publicKeyFilePath);
        } catch (Exception e) {
            LOG.error(ErrorCode.UNABLE_TO_GET_PUBLIC_KEY.getDescription());
            throw new ServiceException(ErrorCode.UNABLE_TO_GET_PUBLIC_KEY, e);
        }
    }

    public PrivateKey getPrivateKey() {
        try {
            return PemUtils.readPrivateKeyFromPEMFile(privateKeyFilePath);
        } catch (Exception e) {
            LOG.error(ErrorCode.UNABLE_TO_GET_PRIVATE_KEY.getDescription());
            throw new ServiceException(ErrorCode.UNABLE_TO_GET_PRIVATE_KEY, e);
        }
    }
}

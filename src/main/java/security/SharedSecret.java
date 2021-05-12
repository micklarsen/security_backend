package security;

import java.security.SecureRandom;

/* This generates a secure random per execution of the server
 * A server restart, will generate a new key, making all existing tokens invalid
 * For production (and if a load-balancer is used) come up with a persistent key strategy */
public class SharedSecret {
    private static byte[] secret;

    public static String getSharedKey() {
      /*
        System.out.println("******************* IMPORTANT ******************'");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("**** REMOVE FIXED SECRET BEFORE PRODUCTION *******");
        System.out.println("****      See security.SharedSecret        *******");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        */
        //REMOVE BEFORE PRODUCTION
        boolean isDeployed = (System.getenv("DEPLOYED") != null);

        if (isDeployed) {
            String secret = System.getenv("SHARED_SECRET");
            return secret;
        } else {
            System.out.println("Not deployed");
            return "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        }
    }
}
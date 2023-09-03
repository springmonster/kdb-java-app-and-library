package com.kdb.connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class KdbConfigTest {

  private final KdbConfig kdbConfig = KdbConfig.create("localhost", 1234, "username", "password");

  @Test
  void getHost_success() {
    assertEquals("localhost", kdbConfig.getHost());
  }

  @Test
  void getHost_failure() {
    assertThrows(NullPointerException.class,
        () -> KdbConfig.create(null, 1234, "username", "password"));
  }

  @Test
  void getPort_success() {
    assertEquals(1234, kdbConfig.getPort());
  }

  @Test
  void getPort_failure() {
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
        () -> KdbConfig.create("localhost", 65536, "username", "password"));

    assertEquals("port 65536 must be between 1 and 65535", illegalArgumentException.getMessage());

    IllegalArgumentException illegalArgumentException1 = assertThrows(
        IllegalArgumentException.class,
        () -> KdbConfig.create("localhost", 0, "username", "password"));

    assertEquals("port 0 must be between 1 and 65535", illegalArgumentException1.getMessage());
  }

  @Test
  void getCredentials_success() {
    assertEquals("username:password", kdbConfig.getCredentials());
  }

  @Test
  void getCredentials_failure() {
    KdbConfig kdbConfig1 = KdbConfig.create("localhost", 1234, "", null);
    assertNotEquals("username:password", kdbConfig1.getCredentials());
  }
}

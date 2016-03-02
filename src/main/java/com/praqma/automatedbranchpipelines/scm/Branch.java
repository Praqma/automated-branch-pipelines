package com.praqma.automatedbranchpipelines.scm;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/** SCM branch information. */
public class Branch {

  private final String urlEncoded;

  Branch(String urlEncoded) {
    this.urlEncoded = urlEncoded;
  }

  public String getUrlEncoded() {
    return urlEncoded;
  }

  public String getBranchForJobName() {
    String urlDecoded = getUrlDecoded();
    return urlDecoded.replaceAll("/", "_");
  }

  private String getUrlDecoded() {
    try {
      return URLDecoder.decode(urlEncoded, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      return urlEncoded.replaceAll("%2F", "/");
    }
  }

  @Override
  public String toString() {
    return getUrlDecoded();
  }
}

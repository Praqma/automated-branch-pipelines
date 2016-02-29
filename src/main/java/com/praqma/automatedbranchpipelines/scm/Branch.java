package com.praqma.automatedbranchpipelines.scm;

/** SCM branch information. */
public class Branch {

  private final String prefix;
  private final String name;

  Branch(String prefix, String name) {
    this.prefix = prefix;
    this.name = name;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return String.format("%s/%s", prefix, name);
  }
}

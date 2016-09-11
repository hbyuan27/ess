package hcm.ess.util;

public enum ERPSystem {
  SF("SuccessFactors"),
  NONE("None");

  private String name;

  private ERPSystem(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}

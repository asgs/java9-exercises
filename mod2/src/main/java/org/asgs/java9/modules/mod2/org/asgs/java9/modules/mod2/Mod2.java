package org.asgs.java9.modules.mod2.org.asgs.java9.modules.mod2;

import org.asgs.java9.modules.mod1.org.asgs.java9.modules.mod1.Mod1;

import java.util.List;

public class Mod2 {
  public static void main(String[] args) {
    System.out.println(
        "I'm another Simple Dummy Java 9 module named mod2 and making use of another dummy module named mod1");
    Mod1.main(List.of("blah").toArray(new String[1]));
  }
}

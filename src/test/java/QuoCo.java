package test;

import core.ClientInfo;
import main.Main;
import org.junit.jupiter.api.Test;

public class QuoCo {

public QuoCo(){

    }
    @Test
    public final void allData() {
        Main main = new Main();
        main.setData("Niki Collier", ClientInfo.FEMALE, 41, 0, 7, "PQR254/1");
    }

    @Test
    public final void withOutGender() {
        Main main = new Main();
        main.setData("Bruce Wayne", ' ', 41, 0, 8, "ABC789/5");
    }

    @Test
    public final void withOutAge() {
        Main main = new Main();
        main.setData("James Bond", ClientInfo.MALE, ' ', 0, 2, "POA567/3");
    }

    @Test
    public final void withOutPoints() {
        Main main = new Main();
        main.setData("Jane Eyer", ClientInfo.FEMALE, 21, ' ', 9, "RQJ389/2");
    }

    @Test
    public final void withOutClaims() {
        Main main = new Main();
        main.setData("Jane Eyer", ClientInfo.FEMALE, 21, 0, ' ', "RQJ389/2");
    }

    @Test
    public final void withOutlicenseNumber() {
        Main main = new Main();
        main.setData("Jane Eyer", ClientInfo.FEMALE, 21, 0, 9, "");
    }
}

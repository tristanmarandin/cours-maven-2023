package fr.imt.cours.IL.Maven.machine.component;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SteamPipeTest {

    @Test
    void testIsOnInitially() {
        SteamPipe steamPipe = new SteamPipe();
        assertFalse(steamPipe.isOn(), "SteamPipe should be off initially");
    }

    @Test
    void testSetOn() {
        SteamPipe steamPipe = new SteamPipe();
        steamPipe.setOn();
        assertTrue(steamPipe.isOn(), "SteamPipe should be on after calling setOn");
    }

    @Test
    void testSetOff() {
        SteamPipe steamPipe = new SteamPipe();
        steamPipe.setOn(); // First, turn it on
        steamPipe.setOff(); // Now, turn it off
        assertFalse(steamPipe.isOn(), "SteamPipe should be off after calling setOff");
    }
}


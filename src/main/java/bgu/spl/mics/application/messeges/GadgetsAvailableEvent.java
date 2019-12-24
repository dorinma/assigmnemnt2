package bgu.spl.mics.application.messeges;

import bgu.spl.mics.Event;

public class GadgetsAvailableEvent implements Event {

    private String gadget;

    public GadgetsAvailableEvent(String gadget) {
        this.gadget = gadget;
    }

    public String getGadget() {
        return gadget;
    }
}
package org.vaadin.gwtav;

public enum Control {

	NODOWNLOAD("nodownload"),
	NOREMOTEPLAYBACK("noremoteplayback"),
	NOFULLSCREEN("nofullscreen");

    private final String control;

    private Control(String control) {
        this.control = control;
    }

    @Override
    public String toString() {
        return control;
    }
}

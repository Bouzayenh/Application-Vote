package controller;

import java.sql.SQLException;

import controller.database.AbstractCBD;

public class CBDScrutateur extends AbstractCBD {

    public CBDScrutateur() throws SQLException {
        super("bouazzatiy", "Azertyuiop");
    }
}

package com.example.wanglixin.slidingmenu;

/**
 * Created by wanglixin on 2017/7/12.
 */

public interface MenuFunction {
    /**
     * open the target menu
     */
    void openMenu();

    /**
     * close the target menu
     */
    void closeMenu();

    /**
     * @return the status of the menu<br>
     * return true if the menu is open,otherwise false.
     */
    boolean isOpen();
}

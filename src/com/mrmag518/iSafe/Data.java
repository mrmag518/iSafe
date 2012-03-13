/*
 * iSafe
 * Copyright (C) 2011-2012 mrmag518 <magnusaub@yahoo.no>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.mrmag518.iSafe;

import java.util.Calendar;

public class Data {
    public static iSafe plugin;
    public Data(iSafe instance)
    {
        plugin = instance;
    }
    
    public static String getDate() {
        String date;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        date = Integer.toString(month);
        date += "/";
        date += calendar.get(Calendar.DAY_OF_MONTH) + "/";
        date += calendar.get(Calendar.YEAR) + " ";
        date += calendar.get(Calendar.HOUR_OF_DAY) + ":";
        date += calendar.get(Calendar.MINUTE) + ".";
        date += calendar.get(Calendar.SECOND) + "";
        return date;
    }
    
    public static String getSig() {
        String signature = "%%%%%%%%%%%%%%%%%¤¤¤¤¤%#&%%&¤%&%/¤#%%%%%%%%%%%%%%";
        return signature;
    }
}

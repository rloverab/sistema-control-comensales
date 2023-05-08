/*
 * Copyright (C) 2022 Roger Lovera <rloverab@yahoo.es>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package clases;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author Roger Lovera <rloverab@yahoo.es>
 */
public class TimestampTools {

    /**
     *
     * @param original
     * @param time
     * @return
     */
    public static Timestamp getTimestamp(long original, long time){
        Calendar calOriginal;
        Calendar calTime;
        Calendar calDateTime;
        
        calOriginal = Calendar.getInstance();
        calTime = Calendar.getInstance();
        calDateTime = Calendar.getInstance();
        calOriginal.setTimeInMillis(original);
        calTime.setTimeInMillis(time);
        calDateTime.set(
                calOriginal.get(Calendar.YEAR), 
                calOriginal.get(Calendar.MONTH), 
                calOriginal.get(Calendar.DAY_OF_MONTH), 
                calTime.get(Calendar.HOUR_OF_DAY), 
                calTime.get(Calendar.MINUTE), 
                calTime.get(Calendar.SECOND));
        calDateTime.set(Calendar.MILLISECOND, 0);
        
        return new Timestamp(calDateTime.getTimeInMillis());
    }
}

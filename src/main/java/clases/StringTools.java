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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Roger Lovera <rloverab@yahoo.es>
 */
public final class StringTools {

    /**
     * Convierte los primeros caracteres de cada palabra en mayúsculas y el resto en minúsculas.
     * @param string
     * @return
     */
    public final static String proper(String string) {
        String[] tokens = string.split(" ");
        String _string = "";

        for (String token : tokens) {
            token = token.trim().toLowerCase();
            token = token.substring(0, 1).toUpperCase() + token.substring(1);

            _string += token + (token.isBlank() ? "" : " ");
        }

        return _string.trim();
    }
    
    /**
     * Evalúa si un caracter es alfanumérico.
     * @param character
     * @return
     */
    public final static boolean isAlphanumeric(char character){
        return isAlphanumeric(character, false);
    }
    
    /**
     * Evalúa si un caracter es alfanumérico.
     * @param character
     * @param whitespace
     * @return
     */
    public final static boolean isAlphanumeric(char character, boolean whitespace){
        return isAlphabetic(character, whitespace) || isNumeric(character);
    }
    
    public final static boolean isAlphanumeric(String text, boolean withSpace){
        for(int i = 0; i < text.length(); i++){
            if(!isAlphanumeric(text.charAt(i))){
                if(!withSpace){
                    return false;                    
                }else{
                    if(text.charAt(i) != ' '){
                        return false;
                    }
                }                
            }
        }
        return true;
    }
    
    /**
     * Evalúa si un caracter es alfabético.
     * @param character
     * @return
     */
    public final static boolean isAlphabetic(char character){
        return isAlphabetic(character, false);
    }
    
    /**
     * Evalúa si un caracter es alfabético.
     * @param character
     * @param whitespace5
     * @return
     */
    public final static boolean isAlphabetic(char character, boolean whitespace){
        List<Character> acentos = Arrays.asList(
                'á','é','í','ó','ú','Á','É','Í','Ó','Ú',
                'à','è','ì','ò','ù','À','È','Ì','Ò','Ù');
        List<Character> dieresis = Arrays.asList(
                'ä','ë','ï','ö','ü','Ä','Ë','Ï','Ö','Ü', 
                'â','ê','î','ô','û','Â','Ê','Î','Ô','Û',
                'ã','õ','Ã','Õ');
        List<Character> espanol = Arrays.asList('ñ','Ñ');
        List<Boolean> conditions;
        
        conditions = new ArrayList<>();
        conditions.add(character >= 'A' && character <= 'Z');
        conditions.add(character >= 'a' && character <= 'z');
        conditions.add(acentos.contains(character));
        conditions.add(dieresis.contains(character));
        conditions.add(espanol.contains(character));
        
        if(whitespace){
            conditions.add(character == ' ');
        }
        
        return conditions.contains(true);
    }
    
    public final static boolean isAlphabetic(char character, String includes){
        if(isAlphabetic(character)){
           return true; 
        }else{
            return includes.indexOf(character) >= 0;
        }
    }
    
    public final static boolean isAlphabetic(String text){
        return isAlphabetic(text, false);
    }
        
    public final static boolean isAlphabetic(String text, boolean whitespace){
        for(int i = 0; i < text.length(); i++){
            if(!isAlphabetic(text.charAt(i))){
                if(!whitespace){
                    return false;                    
                }else{
                    if(text.charAt(i) != ' '){
                        return false;
                    }
                }                
            }
        }
        return true;
    }
    
    public final static boolean isAlphabetic(String text, String includes){
        if(isAlphabetic(text, false)){
            return true;
        }else{
            for (int i = 0; i < includes.length(); i++) {
                if(text.indexOf(includes.charAt(i)) >= 0){
                    return true;
                }                
            }
        }
        
        return false;
    }
        
    /**
     * Evalúa si un caracter es numérico.
     * @param character
     * @return
     */
    public final static boolean isNumeric(char character){        
        return character >= '0' && character <= '9';
    }

    public final static boolean isNumeric(String text, boolean withSpace){
        for(int i = 0; i < text.length(); i++){
            if(!isNumeric(text.charAt(i))){
                if(!withSpace){
                    return false;                    
                }else{
                    if(text.charAt(i) != ' '){
                        return false;
                    }
                }                
            }
        }
        return true;
    }
    
    public final static boolean isEmail(String email){
        Pattern pattern;
        Matcher matcher;
        boolean isEmail;
        
        pattern = Pattern.compile(
                "^[a-zA-Z0-9]{1}[\\w.-]{0,62}[a-zA-Z0-9]{1}[@]{1}[a-zA-Z0-9]{1}[\\w.-]{2,183}[a-zA-Z0-9]{1}[.]{1}[a-zA-Z0-9]{1,3}",
                Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(email);
        isEmail = matcher.matches();
        
        System.out.println(email + "|" + isEmail);
                        
        return isEmail;
    }
}
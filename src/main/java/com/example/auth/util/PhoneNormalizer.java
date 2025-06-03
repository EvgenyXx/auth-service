package com.example.auth.util;




public class PhoneNormalizer {

    private PhoneNormalizer() {
    }

    public static String normalize (String numberPhone){
      String normalNumber = numberPhone.replaceAll("\\D","");
      if (normalNumber.matches("^8\\d{10}$")){
          return "7" + normalNumber.substring(1);
      }
      return normalNumber;
    }
}

package com.example.restapi.viewmodels;

import androidx.lifecycle.ViewModel;
import com.example.restapi.models.Person;
import com.example.restapi.models.WantedPerson;
import com.example.restapi.models.Crypto;
import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private List<Person> personList = new ArrayList<>();
    private List<WantedPerson> wantedList = new ArrayList<>();
    private List<Crypto> cryptoList = new ArrayList<>();

    public List<Person> getPersonList() {
        return personList;
    }

    public List<WantedPerson> getWantedList() {
        return wantedList;
    }

    public List<Crypto> getCryptoList() {
        return cryptoList;
    }

    public void addPerson(Person person) {
        personList.add(person);
    }

    public void addWantedPerson(WantedPerson person) {
        wantedList.add(person);
    }

    public void addCrypto(Crypto crypto) {
        cryptoList.add(crypto);
    }

    public void clearPersonList() {
        personList.clear();
    }

    public void clearWantedList() {
        wantedList.clear();
    }

    public void clearCryptoList() {
        cryptoList.clear();
    }
} 
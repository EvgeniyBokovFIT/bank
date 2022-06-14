package com.example.bank.service;

import com.example.bank.entity.CreditTariff;
import com.example.bank.exception.TariffException;
import com.example.bank.repository.CreditTariffRepository;
import com.example.bank.rest.TariffRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CreditTariffService {

    @Autowired
    CreditTariffRepository creditTariffRepository;

    public Page<CreditTariff> getTariffs(Pageable pageable) {
    return creditTariffRepository.findAll(pageable);
    }

    public void createTariff(TariffRequestDTO request) throws TariffException {
        if(request.getMinTerm() < 0 || request.getMinTerm() < 0 || request.getMinAmount() < 0 || request.getMaxAmount() < 0) {
            throw new TariffException("Сроки и тело кредита не могут быть отрицательными");
        }
        if(request.getMinAmount() > request.getMaxAmount()) {
            throw new TariffException("Минимальная сумма не может быть больше максимальной");
        }
        if(request.getMinTerm() > request.getMaxTerm()) {
            throw new TariffException("Минимальный срок кредита не может быть больше максимального");
        }
        CreditTariff creditTariff = new CreditTariff();
        SetTariffByRequest(request, creditTariff);
    }

    public void updateTariff(TariffRequestDTO request) throws TariffException {
        CreditTariff creditTariff = creditTariffRepository.findCreditTariffById(request.getId());
        if(creditTariff == null) {
            throw new TariffException("Тарифа с id " + request.getId() + " не существует");
        }
        SetTariffByRequest(request, creditTariff);
    }

    private void SetTariffByRequest(TariffRequestDTO request, CreditTariff creditTariff) {
        if(request.getMinAmount() != null) {
            creditTariff.setMinAmount(request.getMinAmount());
        }
        if(request.getMaxAmount() != null) {
            creditTariff.setMaxAmount(request.getMaxAmount());
        }
        if(request.getMinTerm() != null) {
            creditTariff.setMinTerm(request.getMinTerm());
        }
        if(request.getMaxTerm() != null) {
            creditTariff.setMaxTerm(request.getMaxTerm());
        }
        if(request.getMinRate() != null) {
            creditTariff.setMinRate(request.getMinRate());
        }
        if(request.getMaxRate() != null) {
            creditTariff.setMaxRate(request.getMaxRate());
        }
        creditTariffRepository.save(creditTariff);
    }

    public void deleteTariff(TariffRequestDTO request) throws TariffException {
        CreditTariff creditTariff = creditTariffRepository.findCreditTariffById(request.getId());
        if(creditTariff == null) {
            throw new TariffException("Тарифа с id " + request.getId() + " не существует");
        }
        creditTariffRepository.delete(creditTariff);
    }


}

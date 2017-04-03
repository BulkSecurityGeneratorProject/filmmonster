package com.example.filmmonster.service;

import com.example.filmmonster.domain.Payment;
import com.example.filmmonster.repository.PaymentRepository;
import com.example.filmmonster.repository.search.PaymentSearchRepository;
import com.example.filmmonster.service.dto.PaymentDTO;
import com.example.filmmonster.service.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Payment.
 */
@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);
    
    @Inject
    private PaymentRepository paymentRepository;

    @Inject
    private PaymentMapper paymentMapper;

    @Inject
    private PaymentSearchRepository paymentSearchRepository;

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save
     * @return the persisted entity
     */
    public PaymentDTO save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.paymentDTOToPayment(paymentDTO);
        payment = paymentRepository.save(payment);
        PaymentDTO result = paymentMapper.paymentToPaymentDTO(payment);
        paymentSearchRepository.save(payment);
        return result;
    }

    /**
     *  Get all the payments.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        Page<Payment> result = paymentRepository.findAll(pageable);
        return result.map(payment -> paymentMapper.paymentToPaymentDTO(payment));
    }

    /**
     *  Get one payment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PaymentDTO findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        Payment payment = paymentRepository.findOne(id);
        PaymentDTO paymentDTO = paymentMapper.paymentToPaymentDTO(payment);
        return paymentDTO;
    }

    /**
     *  Delete the  payment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.delete(id);
        paymentSearchRepository.delete(id);
    }

    /**
     * Search for the payment corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Payments for query {}", query);
        Page<Payment> result = paymentSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(payment -> paymentMapper.paymentToPaymentDTO(payment));
    }
}

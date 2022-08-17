package com.example.springbatch.config;

import com.example.springbatch.entity.PersonsDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;


public class PersonDetailsProcessor implements ItemProcessor<PersonsDetails,PersonsDetails>{


    @Override
    public PersonsDetails process(PersonsDetails personsDetails) throws Exception {
        return personsDetails;
    }
}

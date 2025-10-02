package com.stegvis_api.stegvis_api.notes.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.notes.dto.NoteFilterDTO;
import com.stegvis_api.stegvis_api.notes.model.Note;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class NoteFilterService {

    private final MongoTemplate mongoTemplate;

    public List<Note> filterNotes(String userId, NoteFilterDTO filterDTO) {
        Criteria criteria = Criteria.where("userId").is(userId);

        if (filterDTO.getSubject() != null && !filterDTO.getSubject().isEmpty()) {
            criteria = criteria.and("subject").regex(filterDTO.getSubject(), "i");
        }

        if (filterDTO.getFromDate() != null || filterDTO.getToDate() != null) {
            Criteria dateCriteria = Criteria.where("dateTime");

            if (filterDTO.getFromDate() != null) {
                dateCriteria = dateCriteria.gte(filterDTO.getFromDate());
            }
            if (filterDTO.getToDate() != null) {
                Instant toInstant = filterDTO.getToDate().plusSeconds(24 * 60 * 60 - 1);
                dateCriteria = dateCriteria.lte(toInstant);
            }

            criteria = criteria.andOperator(dateCriteria);
        }

        Query query = new Query(criteria);
        Sort.Direction direction = filterDTO.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        query.with(Sort.by(direction, "dateTime"));

        List<Note> results = mongoTemplate.find(query, Note.class);

        log.debug("Filtered notes for userId={}, subjectFilter={}, fromDate={}, toDate={}, results={}",
                userId, filterDTO.getSubject(), filterDTO.getFromDate(), filterDTO.getToDate(), results.size());

        return results;
    }
}
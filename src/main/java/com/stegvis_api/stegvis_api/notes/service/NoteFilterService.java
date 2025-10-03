package com.stegvis_api.stegvis_api.notes.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.notes.dto.NoteFilterDTO;
import com.stegvis_api.stegvis_api.notes.dto.NoteResponse;
import com.stegvis_api.stegvis_api.notes.mapper.NoteMapper;
import com.stegvis_api.stegvis_api.notes.model.Note;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class NoteFilterService {

    private final MongoTemplate mongoTemplate;
    private final NoteMapper noteMapper;

    public List<NoteResponse> filterNotes(String userId, NoteFilterDTO filterDTO) {
        Criteria criteria = Criteria.where("userId").is(userId);

        if (filterDTO.subject() != null && !filterDTO.subject().isEmpty()) {
            criteria = criteria.and("subject").regex(filterDTO.subject(), "i");
        }

        if (filterDTO.fromDate() != null || filterDTO.toDate() != null) {
            Criteria dateCriteria = Criteria.where("dateTime");

            if (filterDTO.fromDate() != null) {
                dateCriteria = dateCriteria.gte(filterDTO.fromDate());
            }
            if (filterDTO.toDate() != null) {
                Instant toInstant = filterDTO.toDate().plusSeconds(24 * 60 * 60 - 1);
                dateCriteria = dateCriteria.lte(toInstant);
            }

            criteria = criteria.andOperator(dateCriteria);
        }

        Query query = new Query(criteria);
        Sort.Direction direction = filterDTO.ascending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        query.with(Sort.by(direction, "dateTime"));

        List<Note> results = mongoTemplate.find(query, Note.class);

        log.debug("Filtered notes for userId={}, subjectFilter={}, fromDate={}, toDate={}, results={}",
                userId, filterDTO.subject(), filterDTO.fromDate(), filterDTO.toDate(), results.size());

        return noteMapper.toNoteResponseList(results);
    }
}
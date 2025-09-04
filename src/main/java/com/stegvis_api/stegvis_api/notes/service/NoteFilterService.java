package com.stegvis_api.stegvis_api.notes.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.notes.dto.NoteFilterDTO;
import com.stegvis_api.stegvis_api.notes.model.Note;

@Service
public class NoteFilterService {

    private final MongoTemplate mongoTemplate;

    public NoteFilterService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Note> filterNotes(String userId, NoteFilterDTO filterDTO) {
        Criteria criteria = Criteria.where("userId").is(userId);

        if (filterDTO.getSubject() != null && !filterDTO.getSubject().isEmpty()) {
            criteria = criteria.and("subject").regex(filterDTO.getSubject(), "i");
        }

        if (filterDTO.getFromDate() != null) {
            criteria = criteria.and("dateTimeCreated").gte(filterDTO.getFromDate());
        }
        if (filterDTO.getToDate() != null) {
            criteria = criteria.and("dateTimeCreated").lte(filterDTO.getToDate());
        }

        Query query = new Query(criteria);

        Sort.Direction direction = filterDTO.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;

        String sortBy = filterDTO.getSortBy() != null ? filterDTO.getSortBy().toLowerCase() : "date";

        switch (sortBy) {
            case "subject":
                query.with(Sort.by(direction, "subject"));
                break;
            case "dateupdated":
                query.with(Sort.by(direction, "dateTimeUpdated"));
                break;
            default:
                query.with(Sort.by(direction, "dateTimeCreated"));
                break;
        }

        return mongoTemplate.find(query, Note.class);
    }
}

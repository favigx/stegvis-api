package com.stegvis_api.stegvis_api.notes.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.stegvis_api.stegvis_api.notes.dto.AddNoteCollectionDTO;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteCollectionResponse;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteDTO;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteResponse;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteToCollectionResponse;
import com.stegvis_api.stegvis_api.notes.dto.DeleteNoteResponse;
import com.stegvis_api.stegvis_api.notes.dto.EditNoteDTO;
import com.stegvis_api.stegvis_api.notes.dto.EditNoteResponse;
import com.stegvis_api.stegvis_api.notes.dto.NoteCollectionResponse;
import com.stegvis_api.stegvis_api.notes.dto.NoteResponse;
import com.stegvis_api.stegvis_api.notes.model.Note;
import com.stegvis_api.stegvis_api.notes.model.NoteCollection;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateTime", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Note toNote(AddNoteDTO dto);

    AddNoteResponse toAddResponse(Note note);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateTime", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updateNoteFromDto(EditNoteDTO dto, @MappingTarget Note note);

    EditNoteResponse toEditNoteResponse(Note note);

    NoteResponse toNoteResponse(Note note);

    List<NoteResponse> toNoteResponseList(List<Note> notes);

    @Mapping(target = "deletedAt", expression = "java(java.time.Instant.now().toString())")
    @Mapping(target = "message", constant = "Note har raderats framgångsrikt.")
    DeleteNoteResponse toDeleteNoteResponse(Note note);

    @Mapping(target = "id", source = "note.id")
    @Mapping(target = "userId", source = "note.userId")
    @Mapping(target = "subject", source = "note.subject")
    @Mapping(target = "dateTime", expression = "java(java.time.Instant.now())")
    @Mapping(target = "note", source = "optimizedContent")
    Note toOptimizedNote(Note note, String optimizedContent);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "notes", ignore = true)
    NoteCollection toNoteCollection(AddNoteCollectionDTO dto);

    AddNoteCollectionResponse toAddNoteCollectionResponse(NoteCollection noteCollection);

    @Mapping(target = "noteId", source = "noteId")
    @Mapping(target = "noteCount", expression = "java(collection.getNotes() != null ? collection.getNotes().size() : 0)")
    AddNoteToCollectionResponse toAddNoteToCollectionResponse(NoteCollection collection, String noteId);

    NoteCollectionResponse toNoteCollectionResponse(NoteCollection noteCollection);

    List<NoteCollectionResponse> toNoteCollectionResponseList(List<NoteCollection> notes);
}
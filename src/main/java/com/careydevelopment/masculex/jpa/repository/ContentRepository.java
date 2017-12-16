package com.careydevelopment.masculex.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.careydevelopment.masculex.jpa.entity.Content;

public interface ContentRepository extends BaseRepository<Content, Long> {

	@Query("SELECT c FROM Content c where c.slug=:slug")
	Content fetchBySlug(@Param("slug") String slug);
	
    @Query("SELECT c FROM Content c where c.context.id =:contextId order by c.date desc")
    Page<Content> findContent(Pageable page, @Param("contextId") long contextId);
}

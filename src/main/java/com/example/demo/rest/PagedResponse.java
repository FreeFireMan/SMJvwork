package com.example.demo.rest;

import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PagedResponse<T> extends ResourceSupport {

    private PagedResources.PageMetadata page;
    private Collection<T> content;

    public PagedResponse(PagedResources.PageMetadata page, Collection<T> content) {
        this.page = page;
        this.content = content;
    }

    public PagedResources.PageMetadata getPage() {
        return page;
    }

    public void setPage(PagedResources.PageMetadata page) {
        this.page = page;
    }

    public Collection<T> getContent() {
        return content;
    }

    public void setContent(Collection<T> content) {
        this.content = content;
    }

    public static <T> PagedResponse<T> from(PagedResources<Resource<T>> resources) {
        List<T> unwrapped = resources.getContent().stream().map(Resource::getContent).collect(Collectors.toList());
        PagedResponse<T> resp = new PagedResponse<>(resources.getMetadata(), unwrapped);

        resources.getLinks().forEach(resp::add);
        return resp;
    }
}

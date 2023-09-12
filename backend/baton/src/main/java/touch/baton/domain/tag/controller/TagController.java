package touch.baton.domain.tag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.runnerpost.controller.response.TagSearchResponse;
import touch.baton.domain.runnerpost.controller.response.TagSearchResponses;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.service.TagService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
@RestController
public class TagController {

    private final TagService tagService;

    @GetMapping("/search")
    public ResponseEntity<TagSearchResponses.Detail> readTags(@RequestParam String name) {
        String reducedName = name.replaceAll(" ", "");
        List<Tag> tags = tagService.readTagsByTagName(reducedName);
        List<TagSearchResponse.TagResponse> tagSearchResponses = tags.stream()
                .map(tag -> TagSearchResponse.TagResponse.of(tag.getId(), tag.getTagReducedName().getValue()))
                .toList();

        return ResponseEntity.ok(TagSearchResponses.Detail.from(tagSearchResponses));
    }
}

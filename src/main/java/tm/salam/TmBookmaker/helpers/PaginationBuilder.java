package tm.salam.TmBookmaker.helpers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.helpers.types.SortType;


@Component
public class PaginationBuilder {

    private final SortOption defaultSortOption=new SortOption("created", SortType.DESCENDING);

    public Pageable buildPagination(final int page, final int size, SortOption[] sortOptions) {

        Sort sort=Sort.unsorted();

        if(sortOptions==null){
            sortOptions=new SortOption[]{defaultSortOption};
        }
        for(SortOption sortOption:sortOptions) {
            switch (sortOption.getOrder()) {
                case ASCENDING ->
                    sort = sort.and(Sort.by(Sort.Direction.ASC, sortOption.getField()));
                case DESCENDING ->
                    sort = sort.and(Sort.by(Sort.Direction.DESC, sortOption.getField()));
            }
        }

        return PageRequest.of(page, size, sort);
    }

}

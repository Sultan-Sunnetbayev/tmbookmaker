package tm.salam.TmBookmaker.helpers.types;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortOption {

    private String field;
    private SortType order;


    @Override
    public String toString() {
        return "SortOption{" +
                "field='" + field + '\'' +
                ", order=" + order +
                '}';
    }

}

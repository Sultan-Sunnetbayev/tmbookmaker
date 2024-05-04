package tm.salam.TmBookmaker.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.RegionRepository;
import tm.salam.TmBookmaker.dtoes.serializers.RegionDTOSerializer;
import tm.salam.TmBookmaker.dtoes.models.RegionDTO;
import tm.salam.TmBookmaker.helpers.*;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.Region;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class RegionServiceImpl implements RegionService {

    private final PaginationBuilder paginationBuilder;
    private final RegionRepository regionRepository;
    private final RegionDTOSerializer regionDTOSerializer;


    public RegionServiceImpl(PaginationBuilder paginationBuilder, RegionRepository regionRepository,
                             RegionDTOSerializer regionDTOSerializer) {
        this.regionRepository = regionRepository;
        this.paginationBuilder = paginationBuilder;
        this.regionDTOSerializer = regionDTOSerializer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> addRegion(final Region region){

        final ResponseTransfer<?> responseTransfer;
        final Boolean isAdded=regionRepository.addRegion(region.getName());

        if(Boolean.TRUE.equals(isAdded)){
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.builder()
                            .message("accept region successful added")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error region don't added")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<Integer> getNumberRegionsBySearchKey(String searchKey){

        final ResponseTransfer<Integer>responseTransfer;

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        final int numberRegions=regionRepository.getNumberRegionsBySearchKey(searchKey);

        responseTransfer=ResponseTransfer.<Integer>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<Integer>builder()
                        .message("number regions by search key")
                        .data(numberRegions)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<RegionDTO>> getRegionDTOSBySearchKey(String searchKey, final int page, final int size,
                                                                      SortOption[] sortOptions){

        final ResponseTransfer<List<RegionDTO>> responseTransfer;
        sortOptions=parseRegionSortOption(sortOptions);

        final Pageable pageable=paginationBuilder.buildPagination(page, size, sortOptions);

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        List<Region>regions=regionRepository.getRegionsBySearchKey(pageable, searchKey);
        List<RegionDTO>regionDTOS=new LinkedList<>();

        if(regions!=null){
            for(Region region:regions){
                regionDTOS.add(regionDTOSerializer.toRegionDTOGeneral(region));
            }
        }

        responseTransfer= ResponseTransfer.<List<RegionDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<RegionDTO>>builder()
                        .message("accept all founded regions successful returned")
                        .data(regionDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    private SortOption[] parseRegionSortOption(final SortOption[] sortOptions) {

        if (sortOptions == null) {
            return null;
        }
        List<SortOption>parsedSortOptions=new LinkedList<>();

        for(SortOption sortOption:sortOptions){
            switch(sortOption.getField()){
                case "name" -> {
                    parsedSortOptions.add(sortOption);
                }
            }
        }

        return parsedSortOptions.toArray(SortOption[] ::new);
    }

    @Override
    public ResponseTransfer<RegionDTO> getRegionDTOByUuid(final UUID uuid){

        ResponseTransfer<RegionDTO> responseTransfer;
        Region region=regionRepository.getRegionByUuid(uuid);

        if(region==null){
            responseTransfer= ResponseTransfer.<RegionDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<RegionDTO>builder()
                            .message("error region not found with this uuid")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.<RegionDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<RegionDTO>builder()
                            .message("accept region successful returned with this uuid")
                            .data(regionDTOSerializer.toRegionDTO(region))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> editRegion(final Region region){

        final ResponseTransfer<?> responseTransfer;
        if(region.getUuid()==null){
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .responseBody(ResponseBody.builder()
                            .message("error edited region uuid is invalid")
                            .build())
                    .build();

            return responseTransfer;
        }
        final Boolean isEdited=regionRepository.editRegion(region.getUuid(), region.getName());

        if(isEdited==null || !isEdited){
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error region don't edited")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept region successful edited")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> removeRegionByUuid(final UUID uuid){

        final ResponseTransfer<?> responseTransfer;
        final Boolean isRemoved=regionRepository.removeRegionByUuid(uuid);

        if(isRemoved==null || !isRemoved){
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error region don't removed")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept region successful removed")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<RegionDTO>> getActiveRegionDTOS(){

        final ResponseTransfer<List<RegionDTO>> responseTransfer;

        final List<Region>regions=regionRepository.getActiveRegions();
        final List<RegionDTO>regionDTOS=new LinkedList<>();

        if(regions!=null){
            for(Region region : regions){
                regionDTOS.add(regionDTOSerializer.toRegionDTOOnlyUuidAndName(region));
            }
        }
        responseTransfer= ResponseTransfer.<List<RegionDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<RegionDTO>>builder()
                        .message("accept all founded regions successful returned")
                        .data(regionDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>switchActivationRegionByUuid(final UUID uuid, final boolean isActive){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isSwitched=regionRepository.switchActivationRegionByUuid(uuid, isActive);

        if(isSwitched==null || !isSwitched){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error region activation don't switched")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept region activation successful switched")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

}

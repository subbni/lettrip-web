package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.PlaceCategory;
import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.controller.travel.dto.PlaceDto;
import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    @Transactional
    public Place savePlace(PlaceDto.Request placeDto) {
        try {
            // 이미 존재하는 장소일 경우 찾아서 리턴
            return findPlaceByLocationPoint(
                   getFormatLocationPoint(String.valueOf(placeDto.getXpoint()),String.valueOf(placeDto.getYpoint()))
            );
        } catch(ResourceNotFoundException e) {
            // 존재하지 않을 경우 만들어서 저장 후 리턴
            return placeRepository.save(
                    Place.builder()
                            .name(placeDto.getName())
                            .locationPoint(getFormatLocationPoint(String.valueOf(placeDto.getXpoint()),String.valueOf(placeDto.getYpoint())))
                            .categoryCode(placeDto.getCategoryCode().length()<1?null :
                                    PlaceCategory.getMatchedPlaceCategory(placeDto.getCategoryCode()))
                            .categoryName(
                                    placeDto.getCategoryName().length()<1? null :
                                    PlaceCategory.getMatchedPlaceCategory(placeDto.getCategoryCode())
                                    .getDescription()
                            )
                            .province(Province.of(placeDto.getProvince()))
                            .city(Province.getCityInProvince(
                                    Province.of(placeDto.getProvince()), placeDto.getCity()
                            ))
                            .build()
            );
        }
    }

    public PlaceDto.Response getPlaceByLocationPoint(String xpoint, String ypoint) {
        return new PlaceDto.Response(
                findPlaceByLocationPoint(getFormatLocationPoint(xpoint,ypoint))
        );
    }

    public Point getFormatLocationPoint(String xpoint, String ypoint)  {
        try {
            String pointWKT = String.format("POINT(%s %s)", xpoint, ypoint);
            return (Point) new WKTReader().read(pointWKT);
        } catch(ParseException e) {
            log.error("error during formatting Place Location Point, xpoint:"+xpoint+", ypoint:"+ypoint);
            throw new LettripException(LettripErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    public Place findPlaceByLocationPoint(Point locationPoint) {
        return placeRepository.findByLocationPoint(locationPoint)
                .orElseThrow(()-> {
                    throw new ResourceNotFoundException("Place","locationPoint",locationPoint);
                }
        );
    }

    public Place findById(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Place","id",placeId)
                );
    }

}

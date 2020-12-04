package com.team5.maven.DataFusion.fusers.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.team5.maven.common.MetaData;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

public class UnionFavourSpotify<ValueType, RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends
		ConflictResolutionFunction<List<ValueType>, RecordType, SchemaElementType> {

	public <T> List<T> Intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
	
	@Override
	public FusedValue<List<ValueType>, RecordType, SchemaElementType> resolveConflict(
			Collection<FusibleValue<List<ValueType>, RecordType, SchemaElementType>> values) {

		HashSet<ValueType> union = new HashSet<>();
		

		// Intersection checking
		List<ValueType> intersection = values.iterator().next().getValue();
		
		for (FusibleValue<List<ValueType>, RecordType, SchemaElementType> value : values) {
			if(!intersection.equals(value.getValue())) {
				intersection = Intersection(intersection, value.getValue());
			}
		}		
//		System.out.println(intersection);
//		System.out.println(intersection.isEmpty());
		
		// Union
		if(!intersection.isEmpty()) {			
			for (FusibleValue<List<ValueType>, RecordType, SchemaElementType> value : values) {
				union.addAll(value.getValue());
			}
			List<ValueType> list = new LinkedList<>(union);
			FusedValue<List<ValueType>, RecordType, SchemaElementType> fused = new FusedValue<>(list);
			
			for (FusibleValue<List<ValueType>, RecordType, SchemaElementType> value : values) {
				fused.addOriginalRecord(value);
			}
			
			return fused;
		} 
		
		// Choose from Spotify - if don't have Spotify, take the fist one.
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .appendPattern("yyyy-MM-dd")
		        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
		        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		        .toFormatter(Locale.ENGLISH);
		
		FusibleValue<List<ValueType>, RecordType, SchemaElementType> artist_list = null;
		
		for (FusibleValue<List<ValueType>, RecordType, SchemaElementType> value : values) {
			if (artist_list == null
					|| value.getDateSourceDate().equals(LocalDateTime.parse(MetaData.SPOTIFY_DATE, formatter))) {
				artist_list = value;
			}
		}
		
		return new FusedValue<>(artist_list);
		
		
	}

}
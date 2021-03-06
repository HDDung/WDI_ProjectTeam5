package com.team5.maven.DataFusion.fusers.utility;

import java.util.Collection;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;



public class ModifiedAverage<RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends
ConflictResolutionFunction<Double, RecordType, SchemaElementType> {

	@Override
	public FusedValue<Double, RecordType, SchemaElementType> resolveConflict(
			Collection<FusibleValue<Double, RecordType, SchemaElementType>> values) {

		if (values.size() == 0) {
			return new FusedValue<>((Double) null);
		} else {

			double sum = 0.0;
			double count = 0.0;

			for (FusibleValue<Double, RecordType, SchemaElementType> value : values) {
				if (value.getValue() != -1) {
					sum += value.getValue();
					count++;
				}
			}

			return new FusedValue<>(sum / count);

		}
	}

}

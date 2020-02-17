package org.openmrs.module.eptsreports.reporting.calculation.txml;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.eptsreports.reporting.calculation.generic.MaxLastDateFromFilaSeguimentoRecepcaoCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.TxMLPatientDisagregationProcessor;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class TxMLPatientsWhoRefusedOrStoppedTreatmentCalculation extends BaseFghCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {
    CalculationResultMap resultMap = new CalculationResultMap();

    CalculationResultMap numerator =
        Context.getRegisteredComponents(TxMLPatientsWhoMissedNextApointmentCalculation.class)
            .get(0)
            .evaluate(parameterValues, context);
    Map<Integer, Date> processorResult =
        Context.getRegisteredComponents(TxMLPatientDisagregationProcessor.class)
            .get(0)
            .getPatientsWhoRefusedOrStoppedTreatmentResults(context, numerator);

    // Excluir todos pacientes que fizeram consulta/levantamento apos serem marcados
    // como Stopped/Transfered out
    CalculationResultMap possiblePatientsToExclude =
        Context.getRegisteredComponents(MaxLastDateFromFilaSeguimentoRecepcaoCalculation.class)
            .get(0)
            .evaluate(parameterValues, context);

    CalculationResultMap deadExclusion =
        Context.getRegisteredComponents(TxMLPatientsWhoAreDeadCalculation.class)
            .get(0)
            .evaluate(parameterValues, context);
    CalculationResultMap transferedOutExclusion =
        Context.getRegisteredComponents(TxMLPatientsWhoAreTransferedOutCalculation.class)
            .get(0)
            .evaluate(parameterValues, context);

    for (Integer patientId : processorResult.keySet()) {

      Date candidateDate = processorResult.get(patientId);

      if (!(TxMLPatientDisagregationProcessor.hasPatientsFromOtherDisaggregationToExclude(
              patientId, deadExclusion, transferedOutExclusion)
          || TxMLPatientDisagregationProcessor.hasDatesGreatherThanEvaluatedDateToExclude(
              patientId, candidateDate, possiblePatientsToExclude))) {
        resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
      }
    }
    return resultMap;
  }

  @Override
  public CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context) {
    return this.evaluate(parameterValues, context);
  }
}
package snagicky.collector.api.repo.custom;

import snagicky.collector.api.model.Card;

import java.util.List;
import java.util.Map;

public interface CardRepoCustom {
    List<Card> findCardAdvanced(Map<String,String> request);
}

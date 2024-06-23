package com.communitygreen.cgrn.Ml.dl4j;

import java.io.IOException;

public class InferenceGarDl4j {

  public static void main(String[] args)
      throws IOException, InterruptedException {
//    HotDogOrNotDl4j.training();
    GarOrNotResnet50Dl4j.transferLearningResnet50();
  }
}
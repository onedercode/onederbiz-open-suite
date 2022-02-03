/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2022 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.supplychain.web;

import com.axelor.apps.supplychain.db.Mrp;
import com.axelor.apps.supplychain.db.MrpLine;
import com.axelor.apps.supplychain.db.repo.MrpLineRepository;
import com.axelor.apps.supplychain.db.repo.MrpRepository;
import com.axelor.apps.supplychain.service.MrpLineService;
import com.axelor.apps.supplychain.service.MrpService;
import com.axelor.exception.AxelorException;
import com.axelor.exception.service.HandleExceptionResponse;
import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class MrpLineController {

  @HandleExceptionResponse
  public void generateProposal(ActionRequest request, ActionResponse response)
      throws AxelorException {
    MrpLine mrpLine = request.getContext().asType(MrpLine.class);
    Beans.get(MrpLineService.class)
        .generateProposal(Beans.get(MrpLineRepository.class).find(mrpLine.getId()));
    response.setFlash(I18n.get("The proposal has been successfully generated."));
    response.setReload(true);
  }

  @Transactional(rollbackOn = {Exception.class})
  public void select(ActionRequest request, ActionResponse response) {
    toggle(request, response, true);
  }

  @Transactional(rollbackOn = {Exception.class})
  public void unselect(ActionRequest request, ActionResponse response) {
    toggle(request, response, false);
  }

  @SuppressWarnings("unchecked")
  @Transactional(rollbackOn = {Exception.class})
  private void toggle(ActionRequest request, ActionResponse response, boolean proposalToProcess) {

    List<Integer> mrpLineIds = (List<Integer>) request.getContext().get("_ids");

    if (CollectionUtils.isNotEmpty(mrpLineIds)) {
      Beans.get(MrpLineService.class).updateProposalToProcess(mrpLineIds, proposalToProcess);
    }

    response.setAttr("mrpLinePanel", "refresh", true);
  }

  @Transactional(rollbackOn = {Exception.class})
  public void toggleOne(ActionRequest request, ActionResponse response) {

    MrpLine mrpLine = request.getContext().asType(MrpLine.class);
    mrpLine = Beans.get(MrpLineRepository.class).find(mrpLine.getId());

    Beans.get(MrpLineService.class)
        .updateProposalToProcess(mrpLine, !mrpLine.getProposalToProcess());

    response.setAttr("mrpLinePanel", "refresh", true);
  }

  @Transactional(rollbackOn = {Exception.class})
  public void selectAll(ActionRequest request, ActionResponse response) {

    Mrp mrp = request.getContext().getParent().asType(Mrp.class);
    mrp = Beans.get(MrpRepository.class).find(mrp.getId());

    Beans.get(MrpService.class).massUpdateProposalToProcess(mrp, true);

    response.setAttr("mrpLinePanel", "refresh", true);
  }
}

/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2018 Axelor (<http://axelor.com>).
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
package com.axelor.apps.purchase.script;

import java.util.Map;

import com.axelor.apps.purchase.db.PurchaseOrderLine;
import com.axelor.apps.purchase.service.PurchaseOrderLineService;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;

public class ImportPurchaseOrderLine {

	public Object importPurchaseOrderLine(Object bean, Map<String,Object> values) throws AxelorException {
		assert bean instanceof PurchaseOrderLine;

		PurchaseOrderLine purchaseOrderLine = (PurchaseOrderLine) bean;
		PurchaseOrderLineService purchaseOrderLineService = Beans.get(PurchaseOrderLineService.class);

		purchaseOrderLineService.compute(purchaseOrderLine, purchaseOrderLine.getPurchaseOrder());
		return purchaseOrderLine;
	}
}

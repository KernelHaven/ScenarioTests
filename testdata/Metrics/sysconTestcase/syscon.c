/*
 * System Control Driver
 *
 * Copyright (C) 2012 Freescale Semiconductor, Inc.
 * Copyright (C) 2012 Linaro Ltd.
 *
 * Author: Dong Aisheng <dong.aisheng@linaro.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 */

 /*
  * Copied test case from Linux 4.4.1, won't be used to derive any products
  */
#include <linux/of.h>

static struct syscon *of_syscon_register(struct device_node *np)
{
	struct syscon *syscon;
	struct regmap *regmap;
	void __iomem *base;
	int ret;
	struct regmap_config syscon_config = syscon_regmap_config;

	if (!of_device_is_compatible(np, "syscon"))
		return ERR_PTR(-EINVAL);

	syscon = kzalloc(sizeof(*syscon), GFP_KERNEL);
	if (!syscon)
		return ERR_PTR(-ENOMEM);

	base = of_iomap(np, 0);
	if (!base) {
		ret = -ENOMEM;
		goto err_map;
	}

	/* Parse the device's DT node for an endianness specification */
	if (of_property_read_bool(np, "big-endian"))
		syscon_config.val_format_endian = REGMAP_ENDIAN_BIG;
	 else if (of_property_read_bool(np, "little-endian"))
		syscon_config.val_format_endian = REGMAP_ENDIAN_LITTLE;

	regmap = regmap_init_mmio(NULL, base, &syscon_config);
	if (IS_ERR(regmap)) {
		pr_err("regmap init failed\n");
		ret = PTR_ERR(regmap);
		goto err_regmap;
	}

	syscon->regmap = regmap;
	syscon->np = np;

	spin_lock(&syscon_list_slock);
	list_add_tail(&syscon->list, &syscon_list);
	spin_unlock(&syscon_list_slock);

	return syscon;

err_regmap:
	iounmap(base);
err_map:
	kfree(syscon);
	return ERR_PTR(ret);
}

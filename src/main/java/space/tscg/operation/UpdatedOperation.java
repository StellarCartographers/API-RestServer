/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.operation;

import space.tscg.collections.DiffMap;

public record UpdatedOperation(String uuid, DiffMap map) {
}

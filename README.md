## Overview

This Tetris project implements a variant of the classic game, focusing on decomposing the problem, modular design, and emphasizing design principles. The game involves managing a 2D grid and various-shaped falling pieces with the goal of forming complete rows.

## Game Rules

- Tetris follows the Super Rotation System (SRS) for rotations and wall kicks.
- Controls: A, S, D for left, down, and right movement; Q and E for counterclockwise and clockwise rotation; W to drop the piece.

## Components

1. **TetrisPiece Class:**
   - Represents Tetris pieces.
   - Defines body, bounding box, skirt, rotations, and implements SRS.
   - Efficiently handles constant-time operations.

2. **TetrisBoard Class:**
   - Manages the game board and piece movements.
   - Supports operations like adding pieces, falling, and row clearing.
   - Ensures efficiency in accessing board information.

3. **Brain:**
   - Customizable AI strategy to determine optimal moves (TetrisBot).
   - Implements the Brain interface, offering flexibility for different tactics.


package com.util.handler;

/**
 *  A generic event handler.
 */
@FunctionalInterface
public interface Handler<E> {

  /**
   * Something has happened, so handle it.
   *
   */
  void handle(E event);
}